(ns max-puzzle.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [max-puzzle.events]
              [re-frisk.core :refer [enable-re-frisk!]]
              [max-puzzle.subs]
              [max-puzzle.views :as views]
              [max-puzzle.config :as config]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (enable-re-frisk!)))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
