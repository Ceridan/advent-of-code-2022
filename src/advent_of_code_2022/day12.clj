(ns advent-of-code-2022.day12
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]
            [clojure.string :as str])
  (:import (clojure.lang PersistentQueue)))

(defn- parse-height-map
  [data]
  (let [char-grid (->> (str/split-lines data)
                       (map str/trim)
                       (mapv char-array))
        row-size (count char-grid)
        col-size (count (get char-grid 0))]
  (loop [grid {} S nil E nil x 0 y 0]
    (let [ch (get (get char-grid y) x)]
      (cond (= y row-size) [grid S E]
            (= x col-size) (recur grid S E 0 (inc y))
            (= ch \S) (recur (assoc grid [y x] 0) [y x] E (inc x) y)
            (= ch \E) (recur (assoc grid [y x] (- (int \z) (int \a))) S [y x] (inc x) y)
            :else (recur (assoc grid [y x] (- (int ch) (int \a))) S E (inc x) y))))))

(defn- get-adjacents
  [grid costs pos steps]
  (let [[y x] pos v (get grid pos) cost (inc steps)
        top-pos [(dec y) x] top-v (get grid top-pos) top-cost (get costs top-pos Integer/MAX_VALUE)
        left-pos [y (dec x)] left-v (get grid left-pos) left-cost (get costs left-pos Integer/MAX_VALUE)
        down-pos [(inc y) x] down-v (get grid down-pos) down-cost (get costs down-pos Integer/MAX_VALUE)
        right-pos [y (inc x)] right-v (get grid right-pos) right-cost (get costs right-pos Integer/MAX_VALUE)]
    (->> [(if (and (some? top-v) (< cost top-cost) (<= (- v top-v) 1)) top-pos nil)
          (if (and (some? left-v) (< cost left-cost) (<= (- v left-v) 1)) left-pos nil)
          (if (and (some? down-v) (< cost down-cost) (<= (- v down-v) 1)) down-pos nil)
          (if (and (some? right-v) (< cost right-cost) (<= (- v right-v) 1)) right-pos nil)]
         (filter some?))))

(defn- search
  [grid start-pos end-pos]
  (loop [queue (conj [] [start-pos 0])
         costs {start-pos 0}]
    (if (empty? queue)
      costs
      (let [[curr-pos steps] (peek queue)]
        (cond
          (= curr-pos end-pos) (recur (pop queue) (assoc costs end-pos steps))
          :else (recur
                  (apply conj (pop queue) (map #(vector % (inc steps)) (get-adjacents grid costs curr-pos steps)))
                  (assoc costs curr-pos steps)))))))

(defn part1
  [data]
  (let [[grid S E] (parse-height-map data)]
    (get (search grid E S) S)))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "12"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

