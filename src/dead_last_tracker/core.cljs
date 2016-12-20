(ns dead-last-tracker.core
  (:require [rum.core :as rum]))

(enable-console-print!)

(defn safe-dec [a b]
  (if (neg? (- b a)) 0 (- b a)))

(defonce colors [:green :black :blue :brown :emerald :grey :pink :purple :red :teal :yellow :orange])
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
    :green   :div.ui.green.button
    :black   :div.ui.black.button
    :blue    :div.ui.blue.button
    :brown   :div.ui.brown.button
    :emerald :div.ui.olive.button
    :grey    :div.ui.grey.button
    :pink    :div.ui.pink.button
    :purple  :div.ui.purple.button
    :red     :div.ui.red.button
    :teal    :div.ui.teal.button
    :yellow  :div.ui.yellow.button
    :orange  :div.ui.orange.button
    :div.ui.black.button))

(defn inc-score [color amount]
  (swap! scores (fn [x] (update-in x [color] (partial + amount)))))



(defn dec-score [color amount]
  (swap! scores (fn [x] (update-in x [color] (partial safe-dec amount)))))

(defn reset-score [color]
  (swap! scores (fn [x] (assoc-in x [color] 0))))

(rum/defc score-keeper < rum/reactive [color]
  [:div.one.wide.center.aligned.column 
   [(color-to-label color) [:h1 (color (rum/react scores))]]
   [:br][:br]
   [(color-to-button color) { :on-click (fn [_] (inc-score color 3)) } "+3"]
   [:br][:br]
   [(color-to-button color) { :on-click (fn [_] (inc-score color 4)) } "+4"]
   [:br][:br]
   [(color-to-button color) { :on-click (fn [_] (inc-score color 5)) } "+5"]
   [:br][:br]
   [(color-to-button color) { :on-click (fn [_] (dec-score color 3)) } "-3"]
   [:br][:br]
   [(color-to-button color) { :on-click (fn [_] (dec-score color 4)) } "-4"]
   [:br][:br]
   [(color-to-button color) { :on-click (fn [_] (dec-score color 5)) } "-5"]
   [:br][:br]
   [(color-to-button color) { :on-click (fn [_] (reset-score color)) } "Reset"]])

(rum/defc timer < rum/reactive []
   [:div.sixteen.wide.center.aligned.column
    [:div.ui.circular.massive.label (rum/react countdown)]
    [:br][:br]
    [:div.ui.buttons [
                    [:button.ui.button.active { :on-click (fn [_] (reset! countdown 90)) } "Start"]
                    [:div.or]
                    [:button.ui.positive.button { :on-click (fn [_] (reset! countdown 0)) } "Stop"]]]])

(rum/defc tracker []
  [:div.ui.grid 
   [:div.two.wide.center.aligned.column]
   (map #(score-keeper %) colors)
   [:div.two.wide.center.aligned.column]
   (timer)])

(rum/mount (tracker) (. js/document getElementById "app"))



(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)