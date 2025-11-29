(ns advent-of-code-2022.day19
  (:require [advent-of-code-2022.utils :refer [read-input-as-string-vector]]))

(def time-limit 24)
(def states-limit 100)

(defrecord Factory [robot ore clay obsidian])
(defrecord Blueprint [id ore-factory clay-factory obsidian-factory geode-factory])
(defrecord Resources [ore clay obsidian geode])
(defrecord Robots [ore clay obsidian geode])
(defrecord State [resources robots])

(defn- create-blueprint
  [id ore-ore clay-ore obsidian-ore obsidian-clay geode-ore geode-obsidian]
  (->Blueprint
    (Integer/parseInt id)
    (->Factory :ore (Integer/parseInt ore-ore) 0 0)
    (->Factory :clay (Integer/parseInt clay-ore) 0 0)
    (->Factory :obsidian (Integer/parseInt obsidian-ore) (Integer/parseInt obsidian-clay) 0)
    (->Factory :geode (Integer/parseInt geode-ore) 0 (Integer/parseInt geode-obsidian))))

(defn- parse-blueprints
  [data]
  (->> data
       (map #(re-matches #"Blueprint (\d+): Each ore robot costs (\d+) ore. Each clay robot costs (\d+) ore. Each obsidian robot costs (\d+) ore and (\d+) clay. Each geode robot costs (\d+) ore and (\d+) obsidian." %))
       (map #(rest %))
       (map vec)
       (mapv #(apply create-blueprint %))))

(defn- can-build?
  [blueprint resources robot]
  (cond
    (= robot :ore) (>= (:ore resources) (:ore (:ore-factory blueprint)))
    (= robot :clay) (>= (:ore resources) (:ore (:clay-factory blueprint)))
    (= robot :obsidian) (and (>= (:ore resources) (:ore (:obsidian-factory blueprint))) (>= (:clay resources) (:clay (:obsidian-factory blueprint))))
    (= robot :geode) (and (>= (:ore resources) (:ore (:geode-factory blueprint))) (>= (:obsidian resources) (:obsidian (:geode-factory blueprint))))
    :else false))

(defn- build-robot
  [blueprint resources robot]
  (cond
    (= robot :ore) (assoc resources :ore (- (:ore resources) (:ore (:ore-factory blueprint))))
    (= robot :clay) (assoc resources :ore (- (:ore resources) (:ore (:clay-factory blueprint))))
    (= robot :obsidian) (assoc resources :ore (- (:ore resources) (:ore (:obsidian-factory blueprint))) :clay (- (:clay resources) (:clay (:obsidian-factory blueprint))))
    (= robot :geode) (assoc resources :ore (- (:ore resources) (:ore (:geode-factory blueprint))) :obsidian (- (:obsidian resources) (:obsidian (:geode-factory blueprint))))
    :else resources))

(defn- produce-resources
  [resources robots]
  (assoc resources
    :ore (+ (:ore resources) (:ore robots))
    :clay (+ (:clay resources) (:clay robots))
    :obsidian (+ (:obsidian resources) (:obsidian robots))
    :geode (+ (:geode resources) (:geode robots))))

(defn- calculate-next-states
  [blueprint state]
    (let [{resources :resources robots :robots} state
          can-build? (partial can-build? blueprint resources)
          build-robot (partial build-robot blueprint resources)]
      (->> [(when (can-build? :geode)
               (->State (produce-resources (build-robot :geode) robots) (assoc robots :geode (inc (:geode robots)))))
             (when (can-build? :obsidian)
               (->State (produce-resources (build-robot :obsidian) robots) (assoc robots :obsidian (inc (:obsidian robots)))))
             (when (can-build? :clay)
               (->State (produce-resources (build-robot :clay) robots) (assoc robots :clay (inc (:clay robots)))))
             (when (can-build? :ore)
               (->State (produce-resources (build-robot :ore) robots) (assoc robots :ore (inc (:ore robots)))))
             (->State (produce-resources resources robots) robots)]
           (filterv some?))))

(defn- state-to-vec
  [state]
  (let [res (:resources state)
        rbt (:robots state)]
    (vec
      (interleave
        [(:geode res) (:obsidian res) (:clay res) (:ore res)]
        [(:geode rbt) (:obsidian rbt) (:clay rbt) (:ore rbt)]))))

(defn- state-rev-cmp
  [state1 state2]
  (compare (state-to-vec state2) (state-to-vec state1)))

(defn- choose-top-states
  [states]
  (->> states
       (sort state-rev-cmp)
       (take states-limit)
       (set)))

(defn- simulate-blueprint
  [blueprint state]
  (loop [time 0
         states #{state}]
    (cond
      (= time time-limit) states
      :else (->> states
                 (map #(calculate-next-states blueprint %))
                 (flatten)
                 (choose-top-states)
                 (recur (inc time))))))

(defn- get-max-geodes
  [states]
  (->> states
       (map #(:geode (:resources %)))
       (apply max)))

(defn- process-blueprint
  [blueprint]
  (let [start-state (->State (->Resources 0 0 0 0) (->Robots 1 0 0 0))
        states (simulate-blueprint blueprint start-state)
        geodes (get-max-geodes states)]
    [(:id blueprint) geodes]))

(defn part1
  [data]
  (let [blueprints (parse-blueprints data)]
    (->> blueprints
         (map process-blueprint)
         (map #(apply * %))
         (apply +))))

(defn part2
  [data]
  nil)

(defn -main
  []
  (let [day "19"
        data (read-input-as-string-vector (str "day" day ".txt"))]
    (printf "Day %s, part 1: %s\n", day, (part1 data))
    (printf "Day %s, part 2: %s\n", day, (part2 data))))
