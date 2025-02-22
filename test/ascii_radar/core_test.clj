(ns ascii-radar.core-test
  (:require [clojure.test :refer [deftest is run-tests testing]]
            [clojure.spec.test.alpha :as stest]
            [ascii-radar.core :as core]
            [ascii-radar.sample-data :as sample-data]
            [ascii-radar.core-spec]
            [clojure.string :as str]))

(deftest test-score 
  (testing "testing that score gives 1.0 when both inputs are equal"
    (is (= 1.0
           (core/score (str/join sample-data/invader-1) (str/join sample-data/invader-1))))
    (is (= 1.0
           (core/score sample-data/invader-2 sample-data/invader-2))))
  
  (testing "changing just one location at the end reduces the score appropriately"
    (is (= (float 0.9886364)
           (core/score (str/join sample-data/invader-1)
            (str/join ["--o-----o--"
                       "---o---o---"
                       "--ooooooo--"
                       "-oo-ooo-oo-"
                       "ooooooooooo"
                       "o-ooooooo-o"
                       "o-o-----o-o"
                       "---oo-oo--o"])))))
  
  (testing "changing just one location at the beginning reduces the score appropriately"
    (is (= (float 0.9886364)
           (core/score (str/join sample-data/invader-1)
            (str/join ["o-o-----o--"
                       "---o---o---"
                       "--ooooooo--"
                       "-oo-ooo-oo-"
                       "ooooooooooo"
                       "o-ooooooo-o"
                       "o-o-----o-o"
                       "---oo-oo---"]))))))

(deftest test-sub-sample
  (testing "basic sub-sample"
    (is (= '("----o--oo-"
             "--o-o-----"
             "--o-------"
             "-------o--"
             "------o---"
             "-o--o-----"
             "o---------"
             "--o-------"
             "----------"
             "----------")
           (core/sub-sample sample-data/radar 10 10 0 0)))))

(deftest test-scan-for-invader
  (testing "simplest"
    (is (= [{:location [-1 -1], :score 0.0, :invader "o", :sample "?"}
            {:location [0 -1], :score 0.0, :invader "o", :sample "?"}
            {:location [-1 0], :score 0.0, :invader "o", :sample "?"}
            {:location [0 0], :score 1.0, :invader "o", :sample "o"}]
           
           (core/scan-for-invader ["o"] ["o"] 0.0))))
  
  (testing "invader bigger than radar"
    (is (= [{:location [-1 -2], :score 0.0, :invader "o\no", :sample "?\n?"}
            {:location [0 -2], :score 0.0, :invader "o\no", :sample "?\n?"}
            {:location [-1 -1], :score 0.0, :invader "o\no", :sample "?\n?"}
            {:location [0 -1], :score 0.5, :invader "o\no", :sample "?\no"}
            {:location [-1 0], :score 0.0, :invader "o\no", :sample "?\n?"}
            {:location [0 0], :score 0.5, :invader "o\no", :sample "o\n?"}]
  
           (core/scan-for-invader ["o"] ["o" "o"] 0.0))))
  
  (testing "invader wider than radar"
    (is (= [{:location [-2 -1], :score 0.0, :invader "oo", :sample "??"}
            {:location [-1 -1], :score 0.0, :invader "oo", :sample "??"}
            {:location [0 -1], :score 0.0, :invader "oo", :sample "??"}
            {:location [-2 0], :score 0.0, :invader "oo", :sample "??"}
            {:location [-1 0], :score 0.5, :invader "oo", :sample "?o"}
            {:location [0 0], :score 0.5, :invader "oo", :sample "o?"}]

           (core/scan-for-invader ["o"] ["oo"] 0.0))))
  
  (testing "1 invader off the top"
    (let [x 2 y -1]
      (is (= [[x y]]
             (mapv :location
                   (core/scan-for-invader
                    (core/add-invader-to-radar (core/gen-radar 30 30) sample-data/invader-1 x y)
                    sample-data/invader-1 0.7))))))
  
  (testing "1 invader off the bottom"
    (let [x 2 y 23]
      (is (= [[x y]]
             (mapv :location
                   (core/scan-for-invader
                    (core/add-invader-to-radar (core/gen-radar 30 30) sample-data/invader-1 x y)
                    sample-data/invader-1 0.7))))))
  
  (testing "1 invader off the left"
    (let [x -1 y 13]
      (is (= [[x y]]
             (mapv :location
                   (core/scan-for-invader
                    (core/add-invader-to-radar (core/gen-radar 30 30) sample-data/invader-1 x y)
                    sample-data/invader-1 0.7))))))
  
  (testing "1 invader off the right"
    (let [x 22 y 13]
      (is (= [[x y]]
             (mapv :location
                   (core/scan-for-invader
                    (core/add-invader-to-radar (core/gen-radar 30 30) sample-data/invader-1 x y)
                    sample-data/invader-1 0.7)))))))

(comment
  (deftest run-spec-checks
    (let [results (stest/check [`core/score `core/add-unknowns 
                              ;`core/sub-sample
                                ])]
      (doseq [result results]
        (is (nil? (:failure result))
            (str "Spec check failed for: " (:sym result) "\n" (:failure result))))))
  )

(run-tests)