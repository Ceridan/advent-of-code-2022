(ns advent-of-code-2022.day14
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn- parse-cave-data-line
  [line]
  (loop [cave {}
         points (str/split line #" -> ")]
    (if (= (count points) 1)
      cave
      (let [[lx, ly] (map #(Integer/parseInt %) (str/split (first points) #","))
            [rx, ry] (map #(Integer/parseInt %) (str/split (second points) #","))
            new-rocks (if (= lx rx)
                        [{lx (into #{} (range (min ly ry) (inc (max ly ry))))}]
                        (map #(assoc {} % #{ly}) (range (min lx rx) (inc (max lx rx)))))]
        (recur (reduce #(merge-with into %1 %2) cave new-rocks) (rest points))))))

(defn- parse-cave-data
  [data]
  (loop [cave {}
         lines data]
    (if (= (count lines) 0)
      cave
      (recur (merge-with into cave (parse-cave-data-line (first lines))) (rest lines)))))

(defn- process-send-tile
  [cave x y]
  (if (contains? cave x)
    (let [filtered-y (filter #(> % y) (get cave x))
          min-y (if (empty? filtered-y) -1 (apply min filtered-y))]
      (cond
        (= min-y -1) [-1 -1]
        (not (contains? cave (dec x))) [-1 -1]
        (not (contains? (get cave (dec x)) min-y)) (process-send-tile cave (dec x) min-y)
        (not (contains? cave (inc x))) [-1 -1]
        (not (contains? (get cave (inc x)) min-y)) (process-send-tile cave (inc x) min-y)
        :else [x (dec min-y)]))
    [-1 -1]))

(defn- process-send
  [cave]
  (loop [cave cave
         round 0]
    (let [[x y] (process-send-tile cave 500 0)]
      (if (= [x y] [-1 -1])
        round
        (recur (merge-with into cave {x #{y}}) (inc round))))))

(defn- process-send-tile-floor
  [cave floor-y x y]
  (loop [cave cave
         x x
         y y]
    (cond
      (= y floor-y) [x (dec floor-y)]
      (not (contains? (get cave x) (inc y))) (recur cave x (inc y))
      (not (contains? (get cave (dec x)) (inc y))) (recur cave (dec x) (inc y))
      (not (contains? (get cave (inc x)) (inc y))) (recur cave (inc x) (inc y))
      :else [x y])))

(defn- process-send-floor
  [cave]
  (let [floor-y (+ (apply max (apply set/union (vals cave))) 2)]
    (loop [cave cave
           round 1]
      (let [[x y] (process-send-tile-floor cave floor-y 500 0)]
        (if (= [x y] [500 0])
          round
          (recur (merge-with into cave {x #{y}}) (inc round)))))))

(defn part1
  [data]
  (process-send (parse-cave-data data)))

(defn part2
  [data]
  (process-send-floor (parse-cave-data data)))

(defn -main
  []
  (let [day "14"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
