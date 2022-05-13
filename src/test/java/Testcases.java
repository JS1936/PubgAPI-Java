public class Testcases {

    public static void main(String[] args)
    {
        System.out.println("Beginning to run testcases now");
        access_java_main();

    }

    static void access_java_main()
    {
        System.out.println("\tTesting acccess_java_main:");
        try
        {
            //Main.main();
            Main.initiateFunctionalities();
        }
        catch(UnsupportedOperationException e)
        {
            System.out.println("access_java_main FAILED");
            throw new UnsupportedOperationException("Cannot access method called 'Main.main()'");
        }
        System.out.println("access_java_main PASSED");
        //return true;




    }

}

//EXAMPLE FORMAT?
/*
TEST(problem_4, binary_search_null_array) {
    std::vector<int> lengths = {0, 1, -2};
    for (int length: lengths) {
        int actual = binary_search(nullptr, length, 3);
        ASSERT_EQ(actual, -1);
    }
}
 */