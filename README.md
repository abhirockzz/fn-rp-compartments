# Using Resource Principal Authentication provider with Oracle Functions

When a function you've deployed to [Oracle Functions](https://docs.cloud.oracle.com/iaas/Content/Functions/Concepts/functionsoverview.htm) is running, it can access other Oracle Cloud
Infrastructure resources like Object Storage, Compute, Networking etc. For this, you can use the Resource Principal authentication provider included in the Oracle Cloud Infrastructure SDK.

This example will take you through how to use Oracle Functions to list compartments (and child compartments if any) under your OCI tenancy, using the Resource Principal authentication provider. Let's start by configurion Oracle Functions and deploying our function.

## Oracle Functions

Before you begin, please ensure that you have configure the Oracle Functions development environment. 

- Ensure you are using the latest version of the Fn CLI. To update simply run the following command - `curl -LSs https://raw.githubusercontent.com/fnproject/cli/master/install | sh`
- Oracle Functions setup: Please ensure that you have setup and configured Oracle Functions in your tenancy as per [instructions in the documentation](https://docs.cloud.oracle.com/iaas/Content/Functions/Concepts/functionsprerequisites.htm). 

### Deploy to Oracle Functions

Clone the Git repo and change to the correct directory

	git clone https://github.com/abhirockzz/fn-rp-compartments
	cd fn-rp-compartments

Create an application using the console or CLI.

	fn create app fn-rp-compartments-app --annotation oracle.com/oci/subnetIds='[<SUBNET_OCIDs>]'
        
        //example
        fn create app fn-rp-compartments-app --annotation oracle.com/oci/subnetIds='["ocid1.subnet.oc1.phx.aaaaaaaaghmsma7mpqhqdhbgnby25u2zo4wqlrrcskvu7jg56dryxtfoobar"]' 

Deploy the application

        fn -v deploy --app fn-rp-compartments-app

## Test

Pass in the tenancy (root compartment) OCID

        echo -n '<TENANCY_OCID>' | fn invoke fn-rp-compartments-app listcompartments

        //example
        echo -n 'ocid1.tenancy.oc1..aaaaaaaaydrjm77otncda2xn7qtv7l3hqnd3zxn2u6siwdhniibwfvfoobar' | fn invoke fn-rp-compartments-app listcompartments

You should get back a list of compartment names

        ["comp-1", "comp-2", "comp-3"]
