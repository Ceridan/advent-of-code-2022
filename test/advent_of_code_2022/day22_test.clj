(ns advent-of-code-2022.day22-test
  (:require [clojure.test :refer [deftest testing is]]
            [advent-of-code-2022.day22 :refer [part1 part2]]))

(def monkey-map-example ["        ...#"
                         "        .#.."
                         "        #..."
                         "        ...."
                         "...#.......#"
                         "........#..."
                         "..#....#...."
                         "..........#."
                         "        ...#...."
                         "        .....#.."
                         "        .#......"
                         "        ......#."
                         ""
                         "10R5L5R10L4R5L5"])

(deftest part1-test
  (testing "Part 1"
    (is (= 6032 (part1 monkey-map-example)))))

(deftest part2-test
  (testing "Part 2"
    (is (= nil (part2 "def")))))
