(ns advent-of-code-2022.day11
  (:require [advent-of-code-2022.utils :refer [read-input-as-string]]
            [clojure.string :as str]))

(defprotocol MonkeyInspection
  (operation [this item] "Inspection operation")
  (relief [this item] "Relief after inspection")
  (test-item [this item] "Test the item where to pass it")
  (inspect-all [this] "Inspect all items"))

(defrecord Monkey
  [id items operation-fn test-fn]
  MonkeyInspection
  (operation [_ item] (operation-fn item))
  (relief [_ item] (quot item 3))
  (test-item [_ item] (test-fn item))
  (inspect-all [_] (map #(->> %
                              (operation _)
                              (relief _)
                              ((fn [item] [(test-item _ item) item])))
                        items)))

(defn- parse-monkey
  [data]
  (let [lines (mapv #(str/trim %) (str/split-lines data))
        id (->> (get lines 0) (re-matches #"Monkey (\d+):") second Integer/parseInt)
        items (mapv #(Integer/parseInt %) (-> (get lines 1) (str/replace "Starting items: " "") (str/split #", ")))
        operation-fn (as-> (get lines 2) expr
                           (str/replace expr "Operation: new = " "")
                           (str/split expr #" ")
                           (fn [item] ((resolve (symbol (get expr 1)))
                                       (if (= (get expr 0) "old") item (Integer/parseInt (get expr 0)))
                                       (if (= (get expr 2) "old") item (Integer/parseInt (get expr 2))))))
        test-arg (->> (get lines 3) (re-matches #"Test: divisible by (\d+)") second Integer/parseInt)
        true-monkey (->> (get lines 4) (re-matches #"If true: throw to monkey (\d+)") second Integer/parseInt)
        false-monkey (->> (get lines 5) (re-matches #"If false: throw to monkey (\d+)") second Integer/parseInt)
        test-fn (fn [item] (if (= 0 (mod item test-arg)) true-monkey false-monkey))]
    (->Monkey id items operation-fn test-fn)))

(defn- parse-monkeys
  [data]
  (let [monkeys (str/split data #"\n\n")]
    (->> monkeys
         (mapv parse-monkey)
         (reduce #(assoc %1 (:id %2) %2) (sorted-map)))))

(defn- process-single-monkey
  [monkeys monkey-id]
  (let [inspected (inspect-all (get monkeys monkey-id))]
    (loop [rest-inspected inspected
           monkeys (assoc monkeys monkey-id (assoc (get monkeys monkey-id) :items []))]
    (if (empty? rest-inspected)
      [monkeys (count inspected)]
      (let [[next-monkey-id level] (first rest-inspected)
            next-items (conj (:items (get monkeys next-monkey-id)) level)]
        (recur (rest rest-inspected) (assoc monkeys next-monkey-id (assoc (get monkeys next-monkey-id) :items next-items))))))))

(defn- process-all-monkeys
  [monkeys]
  (let [monkey-count (count monkeys)]
    (loop [round 1
           monkey-id 0
           monkeys monkeys
           counts {}]
      (cond
        (= round 21) counts
        (= monkey-id monkey-count) (recur (inc round) 0 monkeys counts)
        :else (let [[monkeys inspected-count] (process-single-monkey monkeys monkey-id)
                    new-count (+ (get counts monkey-id 0) inspected-count)]
                (recur round (inc monkey-id) monkeys (assoc counts monkey-id new-count)))))))

(defn part1
  [data]
  (->> data
       parse-monkeys
       process-all-monkeys
       vals
       (sort >)
       (take 2)
       (reduce *)))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "11"
        data (read-input-as-string (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
