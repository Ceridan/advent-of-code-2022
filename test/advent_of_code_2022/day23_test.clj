(ns advent-of-code-2022.day23-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day23 :refer [part1 part2]]))

(def plant-map-example ["....#.."
                        "..###.#"
                        "#...#.#"
                        ".#...##"
                        "#.###.."
                        "##.#.##"
                        ".#..#.."])

(deftest part1-test
  (testing "Part 1"
    (is (= 110 (part1 plant-map-example 10)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 20 (part2 plant-map-example)))))
