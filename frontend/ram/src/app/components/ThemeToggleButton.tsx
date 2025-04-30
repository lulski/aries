"use client";

import { ActionIcon, useMantineColorScheme } from "@mantine/core";
import { IconSun, IconMoon } from "@tabler/icons-react";

export default function ThemeToggleButton() {
  const { colorScheme, toggleColorScheme } = useMantineColorScheme();
  const isDark = colorScheme === "dark";

  return (
    <ActionIcon
      onClick={toggleColorScheme}
      variant="outline"
      aria-label="Toggle color scheme"
    >
      {isDark ? <IconSun size={16} /> : <IconMoon size={16} />}
    </ActionIcon>
  );
}
