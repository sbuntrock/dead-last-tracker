(ns dead-last-tracker.core
  (:require [rum.core :as rum]))

(enable-console-print!)

(defn safe-dec [a b]
  (if (neg? (- b a)) 0 (- b a)))

(defonce colors [:red :orange :yellow :emerald :green :teal :blue :purple :pink :brown :grey :black])
(defonce scores (atom (zipmap colors (repeat 0))))
(defonce countdown (atom 0))
(defonce interval (js/setInterval #(swap! countdown (partial safe-dec 1)) 1000))

(defn color-to-label [lbl]
  (case lbl
    :green   :div.ui.green.circular.massive.label
    :black   :div.ui.black.circular.massive.label
    :blue    :div.ui.blue.circular.massive.label
    :brown   :div.ui.brown.circular.massive.label
    :emerald :div.ui.olive.circular.massive.label
    :grey    :div.ui.grey.circular.massive.label
    :pink    :div.ui.pink.circular.massive.label
    :purple  :div.ui.purple.circular.massive.label
    :red     :div.ui.red.circular.massive.label
    :teal    :div.ui.teal.circular.massive.label
    :yellow  :div.ui.yellow.circular.massive.label
    :orange  :div.ui.orange.circular.massive.label
    :div.ui.black.circular.massive.label))

(defn color-to-button [lbl]
  (case lbl
    :green   :div.ui.green.button.tiny
    :black   :div.ui.black.button.tiny
    :blue    :div.ui.blue.button.tiny
    :brown   :div.ui.brown.button.tiny
    :emerald :div.ui.olive.button.tiny
    :grey    :div.ui.grey.button.tiny
    :pink    :div.ui.pink.button.tiny
    :purple  :div.ui.purple.button.tiny
    :red     :div.ui.red.button.tiny
    :teal    :div.ui.teal.button.tiny
    :yellow  :div.ui.yellow.button.tiny
    :orange  :div.ui.orange.button.tiny
    :div.ui.black.button))

(defn inc-score [color amount]
  (swap! scores (fn [x] (update-in x [color] (partial + amount)))))

(defn dec-score [color amount]
  (swap! scores (fn [x] (update-in x [color] (partial safe-dec amount)))))

(defn reset-score [color]
  (swap! scores (fn [x] (assoc-in x [color] 0))))

(rum/defc score-keeper < rum/reactive [color]
  [:div.one.center.aligned.column 
   [(color-to-label color) [:h1.ui.inverted.header (color (rum/react scores))]]
   [:br][:br]
   [(color-to-button color) { :on-click (fn [_] (inc-score color 1)) } [:i.plus.icon]]
   [:br][:br]
   [(color-to-button color) { :on-click (fn [_] (dec-score color 1)) } [:i.minus.icon]]
   [:br][:br]
   [(color-to-button color) { :on-click (fn [_] (reset-score color)) } [:i.undo.icon]]])

(rum/defc timer < rum/reactive []
   [:div.sixteen.wide.center.aligned.column
    [:div.ui.circular.massive.label [:h1.ui.header (rum/react countdown)]]
    [:br][:br]
    [:div.ui.buttons [
                    [:button.ui.positive.button { :on-click (fn [_] (reset! countdown 90)) } "Start"]
                    [:div.or]
                    [:button.ui.negative.button { :on-click (fn [_] (reset! countdown 0)) } "Stop"]]]])

(rum/defc tracker []
  [:div.ui.grid
   [:div.sixteen.wide.center.aligned.column [:h1.ui.header "Dead Last Tracker"]]
   [:div.two.wide.center.aligned.column]
   (map #(score-keeper %) colors)
   [:div.two.wide.center.aligned.column]
   (timer)
   [:div.sixteen.wide.center.aligned.column [:button.ui.black.button {:on-click (fn [_] (reset! scores (zipmap colors (repeat 0))))} "Reset All"]]])

(rum/mount (tracker) (. js/document getElementById "app"))

(defn on-js-reload [])