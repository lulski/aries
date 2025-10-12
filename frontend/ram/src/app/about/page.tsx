"use client";

import {
  Avatar,
  Badge,
  Card,
  Container,
  Divider,
  Group,
  Stack,
  Text,
  Title,
} from "@mantine/core";
import {
  IconBrandGithub,
  IconBrandLinkedin,
  IconMail,
} from "@tabler/icons-react";
import { motion } from "framer-motion";

export default function AboutMePage() {
  return (
    <Container size="md" py="xl">
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
      >
        <Card
          shadow="sm"
          radius="xl"
          p="xl"
          withBorder
          style={{
            backgroundColor: "var(--mantine-color-body)",
          }}
        >
          <Group align="center" gap="lg">
            <Avatar
              src="/images/profile.png"
              alt="Ichsan Siswoputranto"
              size={120}
              radius="xl"
            />
            <Stack gap={4}>
              <Title order={2}>Ichsan Siswoputranto</Title>
              <Text size="sm" c="dimmed">
                Software Engineer & Technical Consultant
              </Text>
              <Group gap="xs" mt="xs">
                <Badge color="blue" variant="light">
                  Java / Spring Boot
                </Badge>
                <Badge color="teal" variant="light">
                  React / Next.js
                </Badge>
                <Badge color="grape" variant="light">
                  Kubernetes / AWS
                </Badge>
              </Group>
            </Stack>
          </Group>

          <Divider my="lg" />

          <Stack gap="sm">
            <Text size="md">
              I'm a Software Engineer and Technical Consultant with over 15
              years of experience designing, building, and optimizing enterprise
              systems across telecommunications, finance, insurance, and
              government sectors.
            </Text>

            <Text size="md">
              I specialize in{" "}
              <strong>Java, Spring Boot, Microservices, and AWS</strong>, with a
              strong focus on system integration, scalability, and security. I
              also work with <strong>React and Next.js</strong> to deliver
              seamless, modern web experiences backed by robust backend
              architectures.
            </Text>

            <Text size="md">
              I'm passionate about solving complex problems, mentoring
              developers, and building scalable, secure systems that make a real
              impact.
            </Text>
          </Stack>

          <Divider my="lg" />

          <Group gap="xl">
            <a
              href="mailto:isiswoputranto@gmail.com"
              style={{ color: "inherit", textDecoration: "none" }}
            >
              <Group gap={4}>
                <IconMail size={20} />
                <Text size="sm">isiswoputranto@gmail.com</Text>
              </Group>
            </a>

            <a
              href="https://github.com/lulski"
              target="_blank"
              rel="noopener noreferrer"
              style={{ color: "inherit", textDecoration: "none" }}
            >
              <Group gap={4}>
                <IconBrandGithub size={20} />
                <Text size="sm">github.com/lulski</Text>
              </Group>
            </a>

            <a
              href="https://linkedin.com/in/msiswoputranto"
              target="_blank"
              rel="noopener noreferrer"
              style={{ color: "inherit", textDecoration: "none" }}
            >
              <Group gap={4}>
                <IconBrandLinkedin size={20} />
                <Text size="sm">linkedin.com/in/msiswoputranto</Text>
              </Group>
            </a>
          </Group>
        </Card>
      </motion.div>
    </Container>
  );
}
