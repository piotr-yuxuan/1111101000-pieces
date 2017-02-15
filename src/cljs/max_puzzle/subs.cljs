(ns max-puzzle.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame :refer [dispatch subscribe]]))

(re-frame/reg-sub
  :name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  :transform
  ;; better with raw sub
  (fn [db [_ id]]
    (merge {:rotate 0
            :translateX 0
            :translateY 0}
           (get-in db [:transforms id]))))

(re-frame/reg-sub
  :css-transform
  (fn [_ [_ id]]
    ;; better with raw sub
    (let [{:keys [rotate translateX translateY]} @(subscribe [:transform id])]
      (str "translateX(" translateX "px) "
           "translateY(" translateY "px) "
           "rotate(" (* -1 (* 90 rotate)) "deg)"))))
