#include <gtest/gtest.h>

#include "Automate.h"

TEST(Exemple, Exemple){
    EXPECT_TRUE(isValid());
}


int main(int argc, char* argv[]) {
  ::testing::InitGoogleTest(&argc, argv);
  return RUN_ALL_TESTS();
}