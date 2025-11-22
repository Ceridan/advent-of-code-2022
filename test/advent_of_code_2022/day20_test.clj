(ns advent-of-code-2022.day20-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day20 :refer [part1 part2]]))

(def nums-example [1 2 -3 3 -2 0 4])

(deftest part1-test
  (testing "Part 1"
    (is (= 3 (part1 nums-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= 1623178306 (part2 nums-example)))))
