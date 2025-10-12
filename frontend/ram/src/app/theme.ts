// theme.ts
import {
  createTheme,
  DefaultMantineColor,
  MantineColorsTuple,
} from "@mantine/core";

export const AriesThemeSet = {
  colors: [
    "paleBlue",
    "paleRed",
    "dark",
    "red",
    "pink",
    "grape",
    "violet",
    "indigo",
    "blue",
    "cyan",
    "green",
    "lime",
    "yellow",
    "orange",
    "teal",
  ] as const satisfies ReadonlyArray<DefaultMantineColor>,
};

const paleBlue: MantineColorsTuple = [
  "#ecf4ff",
  "#dce4f5",
  "#b9c7e2",
  "#94a8d0",
  "#748dc0",
  "#5f7cb7",
  "#5474b4",
  "#44639f",
  "#3a5890",
  "#2c4b80",
];

const paleRed: MantineColorsTuple = [
  "#ffeaf3",
  "#fcd4e1",
  "#f4a7bf",
  "#ec779c",
  "#e64f7e",
  "#e3366c",
  "#e22862",
  "#c91a52",
  "#b41148",
  "#9f003e",
];

export const createAriesTheme = (
  primaryColor: (typeof AriesThemeSet.colors)[number]
) =>
  createTheme({
    colors: {
      paleBlue,
      paleRed,
    },
    primaryColor,
  });
