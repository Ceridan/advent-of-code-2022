(ns advent-of-code-2022.day09-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day09 :refer [part1 part2]]))

(def instructions-example ["R 4"
                           "U 4"
                           "L 3"
                           "D 1"
                           "R 4"
                           "D 1"
                           "L 5"
                           "R 2"])

(deftest part1-test
  (testing "Part 1"
    (is (= 13 (part1 instructions-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= nil (part2 "def")))))
