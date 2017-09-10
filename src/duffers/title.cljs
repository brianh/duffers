(ns duffers.title
  (:require
    [duffers.db :refer [validate-spec]]
    [re-frame.core :refer [reg-sub reg-event-db]]))

(reg-sub
  ::get
  (fn [db _]
    (:title db)))

(reg-event-db
  ::set
  [validate-spec]
  (fn [db [_ value]]
    (assoc db :title value)))
