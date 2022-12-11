(ns advent-of-code-2022.day11
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]
            [clojure.string :as str]))

(defrecord Monkey [id items operation-fn relief-fn test-fn])

(defn- parse-monkey
  [data relief-fn]
  (let [lines (mapv #(str/trim %) (str/split-lines data))
        id (->> (get lines 0) (re-matches #"Monkey (\d+):") second Integer/parseInt)
        items (mapv #(Integer/parseInt %) (-> (get lines 1) (str/replace "Starting items: " "") (str/split #", ")))
        operation-fn (as-> (get lines 2) expr
                           (str/replace expr "Operation: new = " "")
                           (str/split expr #" ")
                           (fn [item] (let [op (get expr 1)
                                            arg1 (if (= (get expr 0) "old") item (Integer/parseInt (get expr 0)))
                                            arg2 (if (= (get expr 2) "old") item (Integer/parseInt (get expr 2)))]
                                        ((resolve (symbol op)) arg1 arg2))))
        test-divisor (->> (get lines 3) (re-matches #"Test: divisible by (\d+)") second Integer/parseInt)
        true-monkey (->> (get lines 4) (re-matches #"If true: throw to monkey (\d+)") second Integer/parseInt)
        false-monkey (->> (get lines 5) (re-matches #"If false: throw to monkey (\d+)") second Integer/parseInt)
        test-fn (fn [item] (if (= 0 (mod item test-divisor)) true-monkey false-monkey))]
    (->Monkey id items operation-fn relief-fn test-fn)))

(defn- parse-monkeys
  [data relief-fn]
  (mapv #(parse-monkey % relief-fn) (str/split data #"\n\n")))

(defn- parse-all-divisors
  [data]
  (->> (re-seq #"divisible by (\d+)" data)
       (map second)
       (map #(Integer/parseInt %))))

(defn- inspect-all-items
  [monkey]
  (->> (:items monkey)
       (map (:operation-fn monkey))
       (map (:relief-fn monkey))
       (mapv #(vector ((:test-fn monkey) %) %))))

(defn- process-single-monkey
  [monkeys monkey-id]
  (let [inspected (inspect-all-items (get monkeys monkey-id))]
    (loop [rest-inspected inspected
           monkeys (assoc-in monkeys [monkey-id :items] [])]
      (if (empty? rest-inspected)
        [monkeys (count inspected)]
        (let [[next-monkey-id level] (first rest-inspected)]
          (recur (rest rest-inspected) (update-in monkeys [next-monkey-id :items] #(conj % level))))))))

(defn- process-all-monkeys
  [rounds monkeys]
  (let [monkey-count (count monkeys)]
    (loop [round 0
           monkey-id 0
           monkeys monkeys
           counts {}]
      (cond
        (= round rounds) counts
        (= monkey-id monkey-count) (recur (inc round) 0 monkeys counts)
        :else (let [[monkeys inspected-count] (process-single-monkey monkeys monkey-id)
                    new-count (+ (get counts monkey-id 0) inspected-count)]
                (recur round (inc monkey-id) monkeys (assoc counts monkey-id new-count)))))))

(defn- calculate-monkey-business
  [monkeys-counts]
  (->> monkeys-counts
       vals
       (sort >)
       (take 2)
       (reduce *)))

(defn part1
  [data]
  (let [monkeys (parse-monkeys data (fn [item] (quot item 3)))]
    (->> (process-all-monkeys 20 monkeys)
         calculate-monkey-business)))

(defn part2
  [data]
  (let [divisor (reduce * (parse-all-divisors data))
        monkeys (parse-monkeys data (fn [item] (mod item divisor)))]
    (->> (process-all-monkeys 10000 monkeys)
         calculate-monkey-business)))

(defn -main
  []
  (let [day "11"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
