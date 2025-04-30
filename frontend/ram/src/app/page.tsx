"use client";
import { Stack, Center, Box, Button, Image, Group } from "@mantine/core";
import { useToggle } from "@mantine/hooks";
import { AriesThemeSet } from "./theme";

export default function Page() {
  const [themeValue, themeToggle] = useToggle(AriesThemeSet.colors);

  return (
    <>
      <Stack>
        <Box w="100%">
          <Button color={themeValue} onClick={() => themeToggle()}>
            how do I use theme
          </Button>
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
