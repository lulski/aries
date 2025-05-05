"use client";

import { Anchor, AppShell, Burger, Group, Stack, Title } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { ReactNode } from "react";
import { AriesLayoutProps } from "@/app/types/AriesLayoutProperties";
import { ThemeToggleButton } from "./ThemeToggleButton/ThemeToggleButton";
import { ThemeProvider } from "../context/ThemeContext";

export default function AriesLayout({ children }: { children: ReactNode }) {
  const [navbarIsOpen, { toggle }] = useDisclosure();

  const layoutProps: AriesLayoutProps = {
    navbarItems: [
      { label: "Home", href: "/" },
      { label: "Posts", href: "/posts" },
      { label: "About", href: "/about" },
    ],
  };

  return (
    <ThemeProvider>
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
                <Title order={5} hiddenFrom="xs">
                  Coco Classico
                </Title>
                <Title order={3} visibleFrom="xs" hiddenFrom="sm">
                  Coco Classico
                </Title>
                <Title order={2} visibleFrom="sm" hiddenFrom="md">
                  Coco Classico
                </Title>
                <Title order={1} visibleFrom="md">
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
          <Anchor size="md" href="/login">
            Login
          </Anchor>
        </AppShell.Navbar>

        <AppShell.Main>{children}</AppShell.Main>
      </AppShell>
    </ThemeProvider>
  );
}
