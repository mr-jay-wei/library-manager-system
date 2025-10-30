package com.nantan.app;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class app {
    public static void main(String[] args) {
        BookRepository repository = null;

        try (Scanner sc = new Scanner(System.in)) {
            // Step 1: 让用户选择数据源
            System.out.println("==============================================");
            System.out.println(" Welcome to the Dual-Database Library Manager ");
            System.out.println("==============================================");
            while (repository == null) {
                System.out.println("请选择要使用的数据库:");
                System.out.println("1. MongoDB");
                System.out.println("2. MySQL (JPA)");
                System.out.print("请输入选项 [1-2]: ");
                try {
                    int dbChoice = sc.nextInt();
                    sc.nextLine(); // 消费换行符

                    // Step 2: 根据选择，实例化对应的DAO实现
                    if (dbChoice == 1) {
                        repository = new BookMongoDAO();
                        System.out.println("--> 已选择 MongoDB 数据库。");
                    } else if (dbChoice == 2) {
                        repository = new BookJpaDAO();
                        System.out.println("--> 已选择 MySQL 数据库。");
                    } else {
                        System.out.println("--> 无效选项，请重新输入。");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("--> 输入错误！请输入一个有效的数字。");
                    sc.nextLine(); // 清除无效的输入
                } catch (Exception e) {
                    // 捕获数据库初始化时可能发生的任何错误 (例如连接失败)
                    System.err.println("--> 数据库初始化失败！请检查配置和网络连接。");
                    e.printStackTrace();
                    return; // 无法初始化，退出程序
                }
            }

            // Step 3: 后续所有操作都通过抽象的 repository 接口进行
            System.out.println(
                    """
                    
                    ----------------------------------------------
                    1. 展示所有书籍
                    2. 添加书籍
                    3. 删除书籍
                    4. 退出应用
                    ----------------------------------------------
                    """
            );

            while (true) {
                System.out.print("\n请输入选项数字: ");
                try {
                    int choice = sc.nextInt();
                    sc.nextLine(); // 消费换行符

                    switch (choice) {
                        case 1 -> {
                            List<Book> allBooks = repository.findAll();
                            if (allBooks.isEmpty()) {
                                System.out.println("--> 数据库中暂无书籍信息");
                            } else {
                                System.out.println("--> 当前书籍列表:");
                                allBooks.forEach(System.out::println);
                            }
                        }
                        case 2 -> {
                            System.out.print("请输入书籍序号: ");
                            int id = sc.nextInt();
                            sc.nextLine();

                            System.out.print("请输入书名: ");
                            String title = sc.nextLine();

                            System.out.print("请输入作者: ");
                            String author = sc.nextLine();

                            Book newBook = new Book(title, author, id);
                            repository.save(newBook);
                            System.out.println("--> 书籍已成功保存到数据库: " + newBook);
                        }
                        case 3 -> {
                            System.out.print("请输入要删除的书籍编号: ");
                            int deleteId = sc.nextInt();
                            sc.nextLine();

                            boolean success = repository.deleteById(deleteId);
                            if (success) {
                                System.out.println("--> 编号为 " + deleteId + " 的书籍已从数据库删除。");
                            } else {
                                System.out.println("--> 未在数据库中找到编号为 " + deleteId + " 的书籍。");
                            }
                        }
                        case 4 -> {
                            System.out.println("--> 退出应用，感谢使用！");
                            repository.close(); // 统一关闭资源
                            return;
                        }
                        default -> System.out.println("--> 无效选项，请输入 1-4 之间的数字。");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("--> 输入错误！请输入一个有效的数字。");
                    sc.nextLine();
                } catch (Exception e) {
                    System.err.println("--> 操作失败，发生了一个错误：");
                    e.printStackTrace();
                }
            }
        }
    }
}