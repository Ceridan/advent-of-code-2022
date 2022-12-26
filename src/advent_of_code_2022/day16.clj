(ns advent-of-code-2022.day16
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]
            [clojure.string :as str]))

(defn- parse-valve-data
  [data]
  (->> data
       (map #(re-matches #"Valve ([A-Z]{2}) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z, ]+)" %))
       (map #(assoc {} :id (get % 1) :rate (Integer/parseInt (get % 2)) :tunnels (str/split (get % 3) #", ")))
       (map #(vector (:id %) %))
       (into {})))

(defn- eliminate-empty-valves
  [valves]
  (->> (vals valves)
       (filter #(or (> (:rate %) 0) (= (:id %) "AA")))
       (map (fn [valve]
              (loop [tunnels (mapv #(vector % 1) (:tunnels valve))
                     visited #{(:id valve)}
                     valve {:id (:id valve) :rate (:rate valve) :tunnels []}]
                (let [[tunnel cost] (first tunnels)]
                  (cond
                    (empty? tunnels) valve
                    (contains? visited tunnel) (recur (vec (rest tunnels)) visited valve)
                    :else (let [next-tunnels (map #(vector % (inc cost)) (:tunnels (get valves tunnel)))]
                            (if (> (:rate (get valves tunnel)) 0)
                              (recur (apply conj (vec (rest tunnels)) next-tunnels) (conj visited tunnel) (update valve :tunnels #(conj % [tunnel cost])))
                              (recur (apply conj (vec (rest tunnels)) next-tunnels) (conj visited tunnel) valve))))))))
       (map #(vector (:id %) %))
       (into {})))

(defn- search-best-score
  [valves state]
  (let [{v :valve t :time visited :visited} state
        details (get valves v)]
    (cond
      (<= t 0) 0
      (contains? visited v) 0
      (= (count valves) (count visited)) 0
      :else (+ (* (:rate details) t)
               (apply max (map #(search-best-score valves {:valve (first %) :time (- (dec t) (second %)) :visited (conj visited v)})
                               (:tunnels details)))))))

(defn- search-best-path
  [valves state]
  (let [{v :valve t :time visited :visited} state
        details (get valves v)]
    (cond
      (<= t 0) [[0 visited]]
      (contains? visited v) [[0 visited]]
      (= (count valves) (count visited)) [[0 visited]]
      :else (let [rate (* (:rate details) t)]
              (->> (:tunnels details)
                   (map #(search-best-path valves {:valve (first %) :time (- (dec t) (second %)) :visited (conj visited v)}))
                   flatten
                   (partition 2)
                   distinct
                   (mapv #(vector (+ (first %) rate) (second %))))))))

(defn part1
  [data]
  (with-redefs [search-best-score (memoize search-best-score)]
    (-> data
        parse-valve-data
        eliminate-empty-valves
        (search-best-score {:valve "AA" :time 30 :visited #{}}))))

(defn part2
  [data]
  (with-redefs [search-best-path (memoize search-best-path)
                search-best-score (memoize search-best-score)]
    (let [valves (-> data parse-valve-data eliminate-empty-valves)]
      (->> (search-best-path valves {:valve "AA" :time 26 :visited #{}})
           (map #(+ (first %) (search-best-score valves {:valve "AA" :time 26 :visited (disj (second %) "AA")})))
           (apply max)))))

(defn -main
  []
  (let [day "16"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
