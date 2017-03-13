(ns max-puzzle.views
  (:require [re-frame.core :refer [dispatch subscribe reg-event-ctx]]
            [reagent.core :as reagent]))

(defn- controlled-fragment
  [{:keys [path centre]}]
  [:img.draggable {:id centre
                   ;; disable html5 drag and drop
                   :draggable false
                   ;;:src "img/bb4.jpg"
                   :src "img/monet.jpg"
                   :style {:max-width "100%"
                           :max-height "100%"
                           :clip-path (str "polygon(" path ")")
                           :-webkit-transform-origin centre
                           :transform @(subscribe [:css-transform centre])}}])

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

(def pieces
  [{:path "  0%   0%,   0%  50%,  50%  50%,  50%   0%" :centre "25% 25%"}
   {:path " 50%   0%,  50%  50%, 100%  50%, 100%   0%" :centre "75% 25%"}
   {:path "  0%  50%,   0% 100%,  50% 100%,  50%  50%" :centre "25% 75%"}
   {:path " 50%  50%,  50% 100%, 100% 100%, 100%  50%" :centre "75% 75%"}])

(defn main-panel []
  (reagent/create-class
    {:component-did-mount #(doall (for [{:keys [centre]} pieces] (watch-for-motion! centre)))
     :reagent-render (fn []
                       [:div.fragments-container
                        [:button {:on-click #(dispatch [:reset-transforms])
                                  :style {:position :absolute
                                          :top 15
                                          :left 15}} "reset position"]
                        (map (fn [{:keys [centre] :as piece}]
                               ^{:key centre} [controlled-fragment piece])
                             pieces)])}))
