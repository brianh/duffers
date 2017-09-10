(ns duffers.db
  (:require
    [clojure.spec.alpha :as s]
    [re-frame.core :refer [reg-event-db ->interceptor]]))

;; spec of app-db
(s/def ::greeting string?)
(s/def ::app-db
  (s/keys :req-un [::title ::golfers]))

;; initial state of app-db
(def app-db {:title "ATM-Memorial Golf Classic"
             :golfers (sort ["Bill" "Chris" "Jeff" "Brian" "Chip" "Dustin" "Mike" "Andy" "Chuck"])})

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
