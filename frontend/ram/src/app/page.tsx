"use client";
import { Box, Image, Stack } from "@mantine/core";
import { useToggle } from "@mantine/hooks";
import { AriesThemeSet } from "./theme";

export default function Aries() {
  const [themeValue, themeToggle] = useToggle(AriesThemeSet.colors);

  return (
    <>
      <Stack>
        <Box w="100%"></Box>
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
