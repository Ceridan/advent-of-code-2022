(ns advent-of-code-2022.day14
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.string :as str]))

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
    (let [min-y (apply min (filter #(> % y) (get cave x)))]
      (cond
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

(defn part1
  [data]
  (process-send (parse-cave-data data)))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "14"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
