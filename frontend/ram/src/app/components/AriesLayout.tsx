"use client";

import {
  Anchor,
  AppShell,
  Burger,
  Group,
  Stack,
  Title,
  useMantineTheme,
} from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { ReactNode } from "react";
import { AriesLayoutProps } from "@/app/types/AriesLayoutProperties";
import { ThemeToggleButton } from "./ThemeToggleButton/ThemeToggleButton";
import { useTheme } from "../context/ThemeContext";

export default function AriesLayout({ children }: { children: ReactNode }) {
  const [navbarIsOpen, { toggle }] = useDisclosure();
  const theme = useTheme();

  const layoutProps: AriesLayoutProps = {
    navbarItems: [
      { label: "main", href: "/" },
      { label: "memo", href: "/posts" },
      { label: "who_am_i", href: "/about" },
      { label: "clicky", href: "/login" },
    ],
  };

  return (
    <AppShell
      suppressHydrationWarning
      header={{ height: 60 }}
      navbar={{
        width: 300,
        breakpoint: "sm",
        collapsed: { mobile: !navbarIsOpen },
      }}
      padding="md"
    >
      <AppShell.Header>
        <Group h="100%" px="md" justify="space-between">
          <Burger
            opened={navbarIsOpen}
            onClick={toggle}
            hiddenFrom="sm"
            size="sm"
          />
          <Group>
            <Stack gap={0}>
              <Title
                order={5}
                hiddenFrom="xs"
                suppressHydrationWarning
                c={theme.primaryColor}
              >
                Coco Classico
              </Title>
              <Title
                order={3}
                visibleFrom="xs"
                hiddenFrom="sm"
                c={theme.primaryColor}
              >
                Coco Classico
              </Title>
              <Title
                order={2}
                visibleFrom="sm"
                hiddenFrom="md"
                c={theme.primaryColor}
              >
                Coco Classico
              </Title>
              <Title order={1} visibleFrom="md" c={theme.primaryColor}>
                Coco Classico
              </Title>
            </Stack>
          </Group>
          <Group>
            <ThemeToggleButton />
          </Group>
        </Group>
      </AppShell.Header>

      <AppShell.Navbar p="md">
        {layoutProps.navbarItems?.map((item, index) => (
          <Anchor size="md" key={index} href={item.href}>
            {item.label}
          </Anchor>
        ))}
      </AppShell.Navbar>

      <AppShell.Main>{children}</AppShell.Main>
    </AppShell>
  );
}
