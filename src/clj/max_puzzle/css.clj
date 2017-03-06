(ns max-puzzle.css
  (:require [garden.def :refer [defstyles]]))

(defstyles screen
           [:body {:width "100%"
                   :height "100%"
                   :margin 0
                   :padding 0}]
           [:#app {:background-color :red}]
           [:.draggable {:user-select :none
                         :-moz-user-select :none
                         :-webkit-user-select :none
                         :-ms-user-select :none
                         :user-drag :none
                         :position :absolute
                         :-moz-user-drag :none
                         :-webkit-user-drag :none
                         :-ms-user-drag :none}]
           [:.fragments-container {:position :absolute
                                   :display :flex
                                   :flex-flow "row wrap"
                                   :align-items :center
                                   :height "100%"
                                   :width "100%"
                                   :justify-content :center}])
