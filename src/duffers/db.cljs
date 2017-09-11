(ns duffers.db
  (:require
    [clojure.spec.alpha :as s]
    [re-frame.core :refer [reg-event-db ->interceptor]]))

;; spec of app-db
(s/def ::title string?)
(s/def ::app-db
  (s/keys :req-un [::title ::golfers]))

(def courses
  ;; course -> [pars]
  {:river [5 4 4 4 3 4 5 4 3 5 4 4 4 3 4 3 4 4]
   :pines [4 3 4 4 5 4 5 3 4 4 5 3 4 4 5 3 4 4]})

;; initial state of app-db
(def app-db {:title "ATM-Memorial Golf Classic"
             :courses courses
             :golfers (sort ["Bill" "Chris" "Jeff" "Brian" "Chip" "Dustin" "Mike" "Andy" "Chuck"])
             :rounds []})

;; start of Datomic schema
#_(def parts [(s/part "app")])

#_(def schema
    [(s/schema golfer
               (s/fields
                 [name :string :indexed]
                 [handicap :int :indexed]))

     (s/schema golf-property
               (s/fields
                 [name :string :indexed]
                 [courses :ref :many]))

     (s/schema course
               (s/fields
                 [name :string :indexed]
                 [holes :ref :many]))

     (s/schema hole
               (s/fields
                 [number :string :indexed]
                 [color :enum [:Black :Blue :White :Gold :Red] :indexed]
                 [handicap :int :indexed]
                 [par :int :indexed]))

     (s/schema round
               (s/fields
                 [golfer :ref]
                 [hole :ref]
                 [score :int :indexed]))])



#_(concat
    (s/generate-parts parts)
    (s/generate-schema schema))

;; -- Interceptors ----------------------------------------------------------
;;
;; See https://github.com/Day8/re-frame/blob/develop/docs/Interceptors.md
;;
(defn check-and-throw
  "Throw an exception if db doesn't have a valid spec."
  [spec db]
  (when-not (s/valid? spec db)
    (let [explain-data (s/explain-data spec db)]
      (throw (ex-info (str "Spec check failed: " explain-data) explain-data)))))

(def validate-spec
  (if goog.DEBUG
    (->interceptor
        :id :validate-spec
        :after (fn [context]
                 (let [db (-> context :effects :db)]
                   (check-and-throw ::app-db db)
                   context)))
    ->interceptor))

(reg-event-db
  :initialize-db
  [validate-spec]
  (fn [_ _]
    app-db))
