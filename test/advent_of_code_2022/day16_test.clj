(ns advent-of-code-2022.day16-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day16 :refer [part1 part2]]))
(def valve-example ["Valve AA has flow rate=0; tunnels lead to valves DD, II, BB"
                    "Valve BB has flow rate=13; tunnels lead to valves CC, AA"
                    "Valve CC has flow rate=2; tunnels lead to valves DD, BB"
                    "Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE"
                    "Valve EE has flow rate=3; tunnels lead to valves FF, DD"
                    "Valve FF has flow rate=0; tunnels lead to valves EE, GG"
                    "Valve GG has flow rate=0; tunnels lead to valves FF, HH"
                    "Valve HH has flow rate=22; tunnel leads to valve GG"
                    "Valve II has flow rate=0; tunnels lead to valves AA, JJ"
                    "Valve JJ has flow rate=21; tunnel leads to valve II"])

(deftest part1-test
  (testing "Part 1"
    (is (= nil (part1 "abc")))))

(deftest part2-test
  (testing "Part 2"
    (is (= nil (part2 "def")))))
