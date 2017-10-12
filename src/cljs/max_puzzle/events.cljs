(ns max-puzzle.events
    (:require [re-frame.core :as re-frame]
              [max-puzzle.db :as db]))

(re-frame/reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-db
  :component-mounted
  (fn [_ _]
    db/default-db))

(re-frame/reg-event-db
  :move
  (fn [db [_ id movmentX movmentY]]
    (update-in db [:transforms id]
               (fn [{:keys [translateX translateY] :as transformation} movmentX movmentY]
                 (assoc transformation
                   :translateX (+ translateX movmentX)
                   :translateY (+ translateY movmentY)))
               movmentX movmentY)))

(re-frame/reg-event-db
  :turn
  (fn [db [_ id]]
    (update-in db [:transforms id :rotate]
               (fn [rotate]
                 (mod (inc rotate) 8)))))

(re-frame/reg-event-db
  :reset-transforms
  (fn [db _]
    (dissoc db :transforms)))

(re-frame/reg-event-db
  :current-top
  (fn [db [_ id]]
    (update-in db
               [:z-index]
               (fn [indices id]
                 (assoc indices id (inc (apply max (vals indices)))))
               id)))
