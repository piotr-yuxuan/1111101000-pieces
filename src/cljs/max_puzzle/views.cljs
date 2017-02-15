(ns max-puzzle.views
  (:require [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(defn- controlled-heckert
  [id]
  [:img {:id id
         ;; disable html5 drag and drop
         :draggable false
         :src "img/bb4.jpg"
         :style {:user-select :none
                 :-moz-user-select :none
                 :-webkit-user-select :none
                 :-ms-user-select :none
                 :user-drag :none
                 :-moz-user-drag :none
                 :-webkit-user-drag :none
                 :-ms-user-drag :none
                 :transform @(subscribe [:css-transform id])}}])

(defn- watch-for-motion!
  "Mouse usually gets out of the picture because OS sometimes likes smooth stuff which breaks all down.
  I can be fix easily with a local state but I prefered to avoid this (I will submit later)
  It would be so very better if it would use a subscription on the element"
  [id]
  (let [el (.getElementById js/document id)
        turn-fn #(dispatch [:turn id])
        track-fn #(dispatch [:move id (goog.object/get % "movementX") (goog.object/get % "movementY")])
        stop-fn #(.removeEventListener el "mousemove" track-fn)
        dont-turn-when-moved-fn (fn introspection []
                                  (.removeEventListener el "mouseup" turn-fn)
                                  (.removeEventListener el "mousemove" introspection))]
    (doto el
      (.addEventListener "mousedown" #(do
                                       (.addEventListener el "mouseup" turn-fn)
                                       (.addEventListener el "mousemove" track-fn)
                                       (.addEventListener el "mousemove" dont-turn-when-moved-fn)))
      (.addEventListener "mouseup" stop-fn)
      (.addEventListener "mouseout" stop-fn))
    ;; makes obvious it returns nil
    nil))

(defn main-panel []
  (let [id "picture"]
    (reagent/create-class
      {:component-did-mount #(watch-for-motion! id)
       :reagent-render (fn []
                         [:div {:style {:display :flex
                                        :justify-content :center}}
                          [controlled-heckert id]])})))
