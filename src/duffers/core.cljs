(ns duffers.core
    (:require [reagent.core :as r :refer [atom]]
              [re-frame.core :refer [subscribe dispatch dispatch-sync]]
              [duffers.db]
              [duffers.golfers]
              [duffers.handlers]
              [duffers.subs]
              [duffers.title]))

(def ReactNative (js/require "react-native"))

(def app-registry (.-AppRegistry ReactNative))
(def text (r/adapt-react-class (.-Text ReactNative)))
(def view (r/adapt-react-class (.-View ReactNative)))
(def image (r/adapt-react-class (.-Image ReactNative)))
(def touchable-highlight (r/adapt-react-class (.-TouchableHighlight ReactNative)))
(def Alert (.-Alert ReactNative))

(defn alert [title]
  (.alert Alert title))

(defn app-root []
  (let [title (subscribe [:duffers.title/get])
        golfers (subscribe [:duffers.golfers/get])]
    (fn []
      [view {:style {:flex-direction "column" :margin 40}}
       ;[image {:source (js/require "./assets/images/cljs.png")
       ;        :style {:width 200
       ;                :height 200}}]
       [text {:style {:font-size 30 :font-weight "100" :margin-bottom 20 :text-align "center"}} @title]
       (for [golfer @golfers]
         [touchable-highlight {:style    {:background-color "#999" :padding 10 :border-radius 5}
                               :on-press #(alert (str "HELLO " golfer))}
          [text {:style {:color "white" :text-align "center" :font-weight "bold"}} golfer]])])))

(defn init []
  (dispatch-sync [:initialize-db])
  (.registerComponent app-registry "main" #(r/reactify-component app-root)))
