(ns advent-of-code-2022.day10
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]
            [clojure.string :as str]))

(defn- parse-cpu-cycles
  [data]
  (map-indexed #(vector (inc %1) (Integer/parseInt %2))
               (-> data
                   (str/replace "\n" " ")
                   (str/replace "noop" "0")
                   (str/replace "addx" "0")
                   str/trim
                   (str/split #" "))))

(defn- calculate-signal-strength
  [cycles]
  (loop [cycles cycles
         X 1
         strength 0]
    (let [[cycle num] (first cycles)
          new-strength (if (= (mod cycle 40) 20) (+ strength (* X cycle)) strength)]
      (if (= cycle 220)
        new-strength
        (recur (rest cycles) (+ X num) new-strength)))))

(defn- calculate-CRT
  [cycles]
  (loop [cycles cycles
         X 1
         CRT []]
    (let [[cycle num] (first cycles)
          col-idx (mod (dec cycle) 40)
          ch (if (contains? #{(dec X) X (inc X)} col-idx) \# \.)]
      (if (= cycle 240)
        (conj CRT ch)
        (recur (rest cycles) (+ X num) (conj CRT ch))))))

(defn- print-CRT
  [CRT]
  (str "\n"
       (->> CRT
            (partition 40)
            (map #(apply str %))
            (str/join "\n"))
       "\n"))

(defn part1
  [data]
  (-> data
      parse-cpu-cycles
      calculate-signal-strength))

(defn part2
  [data]
  (->> data
       parse-cpu-cycles
       calculate-CRT
       print-CRT))

(defn -main
  []
  (let [day "10"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
