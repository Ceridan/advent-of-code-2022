(ns advent-of-code-2022.day24-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day24 :refer [part1 part2]]))

(def blizzard-map-example ["#.######"
                           "#>>.<^<#"
                           "#.<..<<#"
                           "#>v.><>#"
                           "#<^v^^>#"
                           "######.#"])

(deftest part1-test
  (testing "Part 1"
    (is (= 18 (part1 blizzard-map-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= nil (part2 blizzard-map-example)))))
