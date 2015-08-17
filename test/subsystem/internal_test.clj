(ns subsystem.internal-test
  (:require [subsystem.internal :refer :all]
            [clojure.test :refer :all]
            [com.stuartsierra.component :as component]))

(deftest test-component-dependencies
  (testing "When a component depends on :a :b and :c then we should get
           the set #{:a :b :c} back"
    (let [component (component/using {:name "component"} [:a :b :c])]
      (is (= #{:a :b :c} (component-dependencies component)))))
  (testing "When component has no dependencies then we expect the empty set"
    (is (= #{} (component-dependencies {})))))

(def component-a
  (component/using
   {:name "component-a"}
   [:dependency-a]))

(def component-b
  (component/using
   {:name "component-b"}
   [:dependency-a :dependency-b :component-a]))

(def component-c
  {:name "component-c"})

(def component-d
  (component/using
   {:name "component-d"}
   [:component-c]))

(def inner-system
  {:component-a component-a
   :component-b component-b
   :component-c component-c})

(def inner-system-no-ext-deps
  {:component-c component-c
   :component-d component-d})

(deftest test-external-dependencies
  (testing "When some components have dependencies, should return union of all
           component dependencies excluding known components"
    (is (= #{:dependency-a :dependency-b} (external-dependencies inner-system))))

  (testing "When no components have external dependencies, should return empty set"
    (is (= #{} (external-dependencies inner-system-no-ext-deps)))))
