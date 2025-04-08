"use client";

import {
  ActionIcon,
  AppShell,
  Box,
  Burger,
  Container,
  Grid,
  px,
  Text,
} from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { ReactNode } from "react";
import Link from "next/link";
import { AriesLayoutProps } from "@/app/types/AriesLayoutProperties";
import { IconMoodSmileFilled } from "@tabler/icons-react";

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
        <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm" />
        <Box ta={"right"}>
          {" "}
          <ActionIcon
            size={42}
            variant="default"
            aria-label="ActionIcon with size as a number"
          >
            <IconMoodSmileFilled />
          </ActionIcon>
        </Box>
      </AppShell.Header>

      <AppShell.Navbar p="md">
        {navbarItems?.map((item, index) => (
          <Text size="md" key={index}>
            <Link href={item.href}>{item.label}</Link>
          </Text>
        ))}
      </AppShell.Navbar>

      <AppShell.Main>{children}</AppShell.Main>
    </AppShell>
  );
}
