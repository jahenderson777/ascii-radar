(ns ascii-radar.core-spec
  (:require [clojure.spec.alpha :as s]
            [ascii-radar.core :as core]))

(s/def ::a (s/coll-of any?))
(s/def ::b (s/coll-of any?))

(s/fdef core/score
  :args (s/cat :a ::a :b ::b)
  :ret float?)

(s/def ::radar (s/coll-of (s/and string?
                                 #(<= 10 (count %) 200))  :kind vector?
                          :min-count 10 :max-count 200))
(s/def ::w (s/int-in 0 100)) ; Horizontal border width, limited to 0-100
(s/def ::h (s/int-in 0 100)) ; Vertical border height, limited to 0-100
(s/def ::x (s/int-in 0 100)) ; Horizontal offset
(s/def ::y (s/int-in 0 100)) ; Vertical offset

(s/fdef core/add-unknowns
  :args (s/cat :radar ::radar
               :w ::w
               :h ::h)
  :ret (s/coll-of string? :kind sequential?))

(s/fdef core/sub-sample
  :args (s/cat :radar ::radar
               :w ::w
               :h ::h
               :x ::x
               :y ::y)
  :ret (s/coll-of string? :kind sequential?))