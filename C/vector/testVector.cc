#include <gtest/gtest.h>

#include "vector.h"

TEST(Exemple, Exemple){
    EXPECT_TRUE(true);
}


int main(int argc, char* argv[]) {
  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}