(ns advent-of-code-2022.day18
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]))

(defn- parse-cube-data
  [data]
  (->> data
       (map #(re-matches #"(\d+),(\d+),(\d+)" %))
       (map rest)
       (map (fn [cube-pos] (vec (map #(Integer/parseInt %) cube-pos))))))

(defn- get-covered-sides
  [cubes cube]
  (let [[x y z] cube]
    (->> [(contains? cubes [(dec x) y z]) (contains? cubes [(inc x) y z])
          (contains? cubes [x (dec y) z]) (contains? cubes [x (inc y) z])
          (contains? cubes [x y (dec z)]) (contains? cubes [x y (inc z)])]
         (map #(if (true? %) 1 0))
         (reduce +))))

(defn- calculate-covered-sides
  [cubes]
  (let [cubes-set (into #{} cubes)]
    (->> cubes
         (map #(get-covered-sides cubes-set %))
         (reduce +))))

(defn part1
  [data]
  (let [cubes (parse-cube-data data)
        total-sides (* (count cubes) 6)
        covered-sides (calculate-covered-sides cubes)]
    (- total-sides covered-sides)))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "18"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
