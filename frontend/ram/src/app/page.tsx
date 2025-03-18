import { Stack, Center, Box, Button, Image, Group } from "@mantine/core";

export default function Page() {
  return (
    <>
      <style>
        {`
          .parent {
            display: flex;
            justify-content: center;
            align-items: center;
           // height: 100vh;
          }
          
          .title{
            text-align: center;
            //border:1px solid;
          }
          
        `}
      </style>

      <Stack className="">
        <Box w="100%">
          <h1>how do I center a box</h1>
        </Box>
        <Box w="100%">
          <Image
            radius="md"
            src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/images/bg-7.png"
          />
        </Box>
      </Stack>
    </>
  );
}
