(ns advent-of-code-2022.day12
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]
            [clojure.string :as str]))

(defn- parse-height-map
  [data]
  (let [char-grid (->> (str/split-lines data)
                       (map str/trim)
                       (mapv char-array))
        row-size (count char-grid)
        col-size (count (get char-grid 0))]
    (loop [grid {} S nil E nil a #{} x 0 y 0]
      (let [ch (get (get char-grid y) x)
            new-a (if (= ch \a) (conj a [y x]) a)]
        (cond (= y row-size) [grid S E new-a]
              (= x col-size) (recur grid S E new-a 0 (inc y))
              (= ch \S) (recur (assoc grid [y x] 0) [y x] E new-a (inc x) y)
              (= ch \E) (recur (assoc grid [y x] (- (int \z) (int \a))) S [y x] new-a (inc x) y)
              :else (recur (assoc grid [y x] (- (int ch) (int \a))) S E new-a (inc x) y))))))

(defn- get-adjacent
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
  [grid start ends]
  (loop [queue (conj [] [start 0])
         costs {start 0}]
    (if (empty? queue)
      costs
      (let [[curr-pos steps] (peek queue)]
        (if (contains? ends curr-pos)
          (recur (pop queue) (assoc costs curr-pos steps))
          (recur
            (apply conj (pop queue) (map #(vector % (inc steps)) (get-adjacent grid costs curr-pos steps)))
            (assoc costs curr-pos steps)))))))

(defn part1
  [data]
  (let [[grid S E _] (parse-height-map data)]
    (get (search grid E #{S}) S)))

(defn part2
  [data]
  (let [[grid _ E a] (parse-height-map data)
        costs (search grid E a)]
    (->> a
         (map #(get costs %))
         (filter some?)
         (apply min))))

(defn -main
  []
  (let [day "12"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))

