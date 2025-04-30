"use client";

import { Anchor, AppShell, Group, Title } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { ReactNode } from "react";
import { AriesLayoutProps } from "@/app/types/AriesLayoutProperties";
import ThemeToggleButton from "./ThemeToggleButton";
import { theme } from "../theme";

export default function AriesLayout({
  children,
  navbarItems,
}: AriesLayoutProps & { children: ReactNode }) {
  const [opened, { toggle }] = useDisclosure();

  return (
    <AppShell
      header={{ height: 60 }}
      navbar={{
        width: 300,
        breakpoint: "sm",
        collapsed: { mobile: !opened },
      }}
      padding="md"
    >
      <AppShell.Header>
        {/* <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm" /> */}

        <Group h="100%" px="md" justify="space-between">
          <Title>Coco Classico</Title>

          <ThemeToggleButton />

          <Anchor size="md" href="/login">
            Login
          </Anchor>
        </Group>
      </AppShell.Header>

      <AppShell.Navbar p="md">
        {navbarItems?.map((item, index) => (
          <Anchor size="md" key={index} href={item.href}>
            {item.label}
          </Anchor>
        ))}
      </AppShell.Navbar>

      <AppShell.Main>{children}</AppShell.Main>
    </AppShell>
  );
}
