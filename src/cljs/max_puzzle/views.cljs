(ns max-puzzle.views
  (:require [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]))

(defn- controlled-fragment
  [polygon-id]
  [:img.draggable {:id polygon-id
                   ;; disable html5 drag and drop
                   :draggable false
                   ;;:src "img/bb4.jpg"
                   :src "img/monet.jpg"
                   :style {:max-width "100%"
                           :max-height "100%"
                           :clip-path (str "polygon(" polygon-id ")")
                           :transform @(subscribe [:css-transform polygon-id])}}])

(defn- watch-for-motion!
  "Mouse usually gets out of the picture because OS sometimes likes smooth stuff which breaks all down.
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
  (let [piece1 "0 0, 0% 100%, 100% 100%"
        piece2 "0 0, 100% 0, 100% 100%"]
    (reagent/create-class
      {:component-did-mount #(do (watch-for-motion! piece1)
                                 (watch-for-motion! piece2))
       :reagent-render (fn []
                         [:div.fragments-container
                          [controlled-fragment piece1]
                          [controlled-fragment piece2]])})))
