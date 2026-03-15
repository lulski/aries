import type { Meta, StoryObj } from "@storybook/nextjs-vite";

import ImageGalery from "./ImageGalery";

const meta = {
  title: "Components/ImageGalery",
  component: ImageGalery,
  tags: ["autodocs"],
} satisfies Meta<typeof ImageGalery>;

export default meta;

type Story = StoryObj<typeof meta>;

export const Default: Story = {};
