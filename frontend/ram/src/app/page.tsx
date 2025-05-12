"use client";
import {
  Stack,
  Center,
  Box,
  Button,
  Image,
  Group,
  useMantineColorScheme,
} from "@mantine/core";
import { useToggle } from "@mantine/hooks";
import { AriesThemeSet } from "./theme";

export default function Page() {
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
