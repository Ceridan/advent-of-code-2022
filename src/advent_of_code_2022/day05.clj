(ns advent-of-code-2022.day05
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.string :as str]))

(defrecord Instruction [amount from to])

(defn- parse-stack-line
  [line num]
  (take num (concat
              (->> (char-array line)
                   (partition 4 4 [\space])
                   (mapv #(if (= \space (second %)) '() (list (second %)))))
              (repeat '()))))

(defn- parse-stacks
  [stacks]
  (let [num (Integer/parseInt (last (re-matches #".*(\d+) ?$" (last stacks))))]
    (->> stacks
         drop-last
         (map #(parse-stack-line % num))
         (apply map list)
         (map #(reduce into '() %))
         (map reverse)
         (zipmap (iterate inc 1)))))

(defn- parse-instructions
  [instructions]
  (->> instructions
       (map #(re-matches #"move (\d+) from (\d+) to (\d+)" %))
       (map rest)
       (map (fn [nums] (map #(Integer/parseInt %) nums)))
       (map #(apply ->Instruction %))))

(defn- parse-data
  [data]
  (->> data
       (partition-by #(str/blank? %))
       (remove #(str/blank? (first %)))))

(defn- move
  [stacks instruction crane-type]
  (let [to (:to instruction)
        from (:from instruction)
        amount (:amount instruction)
        to-stack (get stacks to)
        from-stack (get stacks from)
        order-fn (fn [seq] (if (= crane-type 9000) seq (reverse seq)))]
    (-> stacks
        (assoc to (into to-stack (order-fn (take amount from-stack))))
        (assoc from (drop amount from-stack)))))

(defn- process-instructions
  [stacks instructions crane-type]
  (loop [stacks stacks
         instructions instructions]
    (if (= 0 (count instructions))
      stacks
      (recur (move stacks (first instructions) crane-type) (rest instructions)))))

(defn- run-crane
  [data crane-type]
  (let [[raw-stacks, raw-instructions] (parse-data data)
        stacks (parse-stacks raw-stacks)
        instructions (parse-instructions raw-instructions)]
    (->> (process-instructions stacks instructions crane-type)
         (into (sorted-map))
         vals
         (map first)
         (apply str))))

(defn part1
  [data]
  (run-crane data 9000))

(defn part2
  [data]
  (run-crane data 9001))

(defn -main
  []
  (let [day "05"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
