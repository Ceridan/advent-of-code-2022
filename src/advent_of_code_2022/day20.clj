(ns advent-of-code-2022.day20
  (:require [advent-of-code-2022.utils :refer [read-input-as-integer-vector]]))

(defn- mix-num
  [numbers positions idx]
  (let [size (count numbers)
        cur-pos (get positions idx)
        cur-num (get numbers idx)
        new-pos (mod (+ cur-pos cur-num) (dec size))]
    (cond
      (= cur-num 0) positions
      (= cur-pos new-pos) positions
      :else
      (-> positions
          (update-vals
            #(cond
               (and (> % cur-pos) (<= % new-pos)) (dec %)
               (and (< % cur-pos) (>= % new-pos)) (inc %)
               :else %))
          (assoc idx new-pos)))))

(defn- mapv-num-to-pos
  [numbers positions]
  (let [size (count numbers)
        init-vec (vec (repeat size 0))]
    (reduce (fn [acc idx] (assoc acc (get positions idx) (get numbers idx))) init-vec (range size))))

(defn part1
  [data]
  (let [numbers data
        positions (vec (range (count numbers)))
        mixed-positions (reduce (partial mix-num numbers) positions positions)
        mixed-numbers (mapv-num-to-pos numbers mixed-positions)]
    (->> mixed-numbers
         cycle
         (drop-while #(not= % 0))
         (take-nth 1000)
         (drop 1)
         (take 3)
         (apply +))))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "20"
        data (read-input-as-integer-vector (str "day" day ".txt"))
        ]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
