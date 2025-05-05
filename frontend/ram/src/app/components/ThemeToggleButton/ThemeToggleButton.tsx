import {
  ActionIcon,
  useComputedColorScheme,
  useMantineColorScheme,
  useMantineTheme,
} from "@mantine/core";
import { IconSun, IconMoon, IconPalette } from "@tabler/icons-react";
import cx from "clsx";
import classes from "./ThemeToggleButton.module.css";
import { AriesThemeSet } from "@/app/theme";
import { useTheme } from "@/app/context/ThemeContext";

export function ThemeToggleButton() {
  const computedColorScheme = useComputedColorScheme("light", {
    getInitialValueInEffect: true,
  });

  const { colorScheme, setColorScheme } = useMantineColorScheme();

  const theme = useMantineTheme();

  const { primaryColor, setPrimaryColor } = useTheme();

  const cycleColor = () => {
    const currentIndex = AriesThemeSet.colors.indexOf(primaryColor);
    const nextIndex = (currentIndex + 1) % AriesThemeSet.colors.length;
    setPrimaryColor(AriesThemeSet.colors[nextIndex]);
  };

  return (
    <>
      <ActionIcon
        onClick={() =>
          setColorScheme(computedColorScheme === "light" ? "dark" : "light")
        }
        variant="default"
        size="xl"
        aria-label="Toggle color scheme"
      >
        <IconSun className={cx(classes.icon, classes.light)} stroke={1.5} />
        <IconMoon className={cx(classes.icon, classes.dark)} stroke={1.5} />
      </ActionIcon>

      <ActionIcon
        onClick={cycleColor}
        variant="filled"
        size="xl"
        color={primaryColor}
        aria-label="Change theme color"
      >
        <IconPalette stroke={1.5} />
      </ActionIcon>
    </>
  );
}
