(ns advent-of-code-2022.day18
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]))

(defn- parse-cube-data
  [data]
  (->> data
       (map #(re-matches #"(\d+),(\d+),(\d+)" %))
       (map rest)
       (map (fn [cube-pos] (vec (map #(Integer/parseInt %) cube-pos))))
       (into #{})))

(defn- get-adjacent
  [cube]
  (let [[x y z] cube]
    [[(dec x) y z] [(inc x) y z]
     [x (dec y) z] [x (inc y) z]
     [x y (dec z)] [x y (inc z)]]))

(defn- get-covered-sides
  [cubes cube]
  (->> (get-adjacent cube)
       (map #(contains? cubes %))
       (map #(if (true? %) 1 0))
       (reduce +)))

(defn- calculate-covered-sides
  [cubes]
  (->> cubes
       (map #(get-covered-sides cubes %))
       (reduce +)))

(defn- get-dimensions
  [cubes]
  [[(apply min (map first cubes)) (apply max (map first cubes))]
   [(apply min (map second cubes)) (apply max (map second cubes))]
   [(apply min (map last cubes)) (apply max (map last cubes))]])

(defn- get-all-cubes
  [dimensions]
  (let [[[min-x max-x] [min-y max-y] [min-z max-z]] dimensions]
    (for [x (range min-x (inc max-x))
          y (range min-y (inc max-y))
          z (range min-z (inc max-z))] [x y z])))

(defn- connect-external
  [cubes]
  (let [dimensions (get-dimensions cubes)
        [[min-x max-x] [min-y max-y] [min-z max-z]] dimensions
        all-cubes (get-all-cubes dimensions)
        borders (filter #(or (= (first %) min-x) (= (first %) max-x)
                             (= (second %) min-y) (= (second %) max-y)
                             (= (last %) min-z) (= (last %) max-z)) all-cubes)]
    (loop [queue borders
           visited (into {} (map #(vector % (contains? cubes %)) all-cubes))]
      (let [cube (first queue)
            [x y z] cube]
        (cond
          (empty? queue) visited
          (true? (get visited cube)) (recur (rest queue) visited)
          (or (< x min-x) (> x max-x)) (recur (rest queue) visited)
          (or (< y min-y) (> y max-y)) (recur (rest queue) visited)
          (or (< z min-z) (> z max-z)) (recur (rest queue) visited)
          :else (recur (apply conj queue (get-adjacent cube))
                       (assoc visited cube true)))))))

(defn part1
  [data]
  (let [cubes (parse-cube-data data)
        total-sides (* (count cubes) 6)
        covered-sides (calculate-covered-sides cubes)]
    (- total-sides covered-sides)))

(defn part2
  [data]
  (let [cubes (parse-cube-data data)
        pockets (keys (filter #(false? (val %)) (connect-external cubes)))
        cubes-with-pockets (into cubes pockets)
        total-sides (* (count cubes-with-pockets) 6)
        covered-sides (calculate-covered-sides cubes-with-pockets)]
    (- total-sides covered-sides)))

(defn -main
  []
  (let [day "18"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
