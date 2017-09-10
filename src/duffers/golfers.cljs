(ns duffers.golfers
  (:require
    [duffers.db :refer [validate-spec]]
    [re-frame.core :refer [reg-event-db reg-sub]]))

(reg-event-db
  ::set
  [validate-spec]
  (fn [db [_ value]]
    (assoc db :golfers value)))

(reg-sub
  ::get
  (fn [db _]
    (:golfers db)))
