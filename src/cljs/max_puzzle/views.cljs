(ns max-puzzle.views
  (:require [re-frame.core :refer [dispatch subscribe reg-event-ctx]]
            [clojure.math.combinatorics :refer [cartesian-product]]
            [reagent.core :as reagent]
            [clojure.string :as str]))

(defn- controlled-fragment
  [{:keys [path centre]}]
  [:img.draggable {:id centre
                   ;; disable html5 drag and drop
                   :draggable false
                   ;;:src "img/bb4.jpg"
                   :src "img/titien.jpg"
                   :style {:max-width "100%"
                           :max-height "100%"
                           :z-index @(subscribe [:z-index centre])
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
                                        (dispatch [:current-top id])
                                        (.addEventListener el "mouseup" turn-fn)
                                        (.addEventListener el "mousemove" track-fn)
                                        (.addEventListener el "mousemove" dont-turn-when-moved-fn)))
      (.addEventListener "mouseup" stop-fn)
      (.addEventListener "mouseout" stop-fn))
    ;; makes obvious it returns nil
    nil))

(defn- partition-number
  [part-number unit]
  (let [partition (/ unit part-number)]
    (loop [result ()
           current part-number]
      (if (neg-int? current)
        result
        (recur (conj result (double (* current partition))) (dec current))))))

(defn- path
  [[x0 y0] [x1 y1]]
  (str (str x0 "%") " " (str y0 "%") ", "
       (str x0 "%") " " (str y1 "%") ", "
       (str x1 "%") " " (str y1 "%") ", "
       (str x1 "%") " " (str y0 "%")))

(defn- average
  [& args]
  (double (/ (reduce + args) (count args))))

(defn- centre
  [[x0 y0] [x1 y1]]
  (str/join " " [(str (average x0 x1) "%")
                 (str (average y0 y1) "%")]))

(defn pieces-cutter
  [step]
  (->> (partition-number step 100)
       (partition 2 1)
       (#(cartesian-product % %))
       (reduce (fn [acc [[x0 x1] [y0 y1] :as i]]
                 (let [path (path [x0 y0] [x1 y1])
                       centre (centre [x0 y0] [x1 y1])]
                   (conj acc {:centre (str centre)
                              :path (str path)})))
               ())))

(def pieces
  (pieces-cutter 5))

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
