(ns advent-of-code-2022.day14-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day14 :refer [part1 part2]]))

(def cave-example ["498,4 -> 498,6 -> 496,6"
                   "503,4 -> 502,4 -> 502,9 -> 494,9"])

(deftest part1-test
  (testing "Part 1"
    (is (= 24 (part1 cave-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= nil (part2 "def")))))
