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

(defn- search-best-path-impl
  [valves visited current time score]
  (let [valve (get valves current)
        new-time (if (contains? visited current) time (dec time))
        new-score (if (contains? visited current) score (+ score (* (:rate valve) new-time)))
        new-visited (conj visited current)]
    (cond
      (<= time 0) score
      (= (count new-visited) (count valves)) new-score
      :else (let [tunnels (remove #(contains? new-visited (first %)) (:tunnels valve))]
              (apply max (map #(search-best-path-impl valves new-visited (first %) (- new-time (second %)) new-score) tunnels))))))

(defn- search-best-path
  [valves]
  (search-best-path-impl valves #{"AA"} "AA" 30 0))

(defn part1
  [data]
  (-> data
      parse-valve-data
      eliminate-empty-valves
      search-best-path))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "16"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
