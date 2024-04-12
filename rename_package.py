import os
import re

def rename_packages(path, old_packages, new_package_name, package_description=None):
    """
    Renomeia o nome do pacote em várias classes Java encontradas no diretório especificado.

    Args:
        path (str): O caminho para o diretório contendo as classes Java.
        old_packages (list): Uma lista de nomes de pacotes antigos a serem substituídos.
        new_package_name (str): O novo nome do pacote.
        package_description (str, opcional): Uma descrição para adicionar acima da declaração de pacote nas classes Java. Padrão é None.

    Returns:
        None

    """
    if not os.path.exists(path):
        print("O caminho fornecido não existe.")
        return

    if not os.path.isdir(path):
        print("O caminho fornecido não é um diretório.")
        return

    # Lista todos os arquivos Java no diretório e subdiretórios
    java_files = []
    for root, dirs, files in os.walk(path):
        for file in files:
            if file.endswith(".java"):
                java_files.append(os.path.join(root, file))

    # Expressões regulares para encontrar importações e declarações de pacotes
    import_regex = re.compile(r'import\s+(?:' + '|'.join(map(re.escape, old_packages)) + r')\.')
    package_regex = re.compile(r'package\s+(?:' + '|'.join(map(re.escape, old_packages)) + r');')

    # Para cada arquivo Java, substitui o nome do pacote antigo pelo novo
    for java_file in java_files:
        # Obter o novo nome do pacote com base na estrutura de diretórios
        relative_path = os.path.relpath(os.path.dirname(java_file), path)
        new_package_path = new_package_name + '.' + relative_path.replace(os.path.sep, '.')

        with open(java_file, 'r') as file:
            file_data = file.read()

        # Substituir importações
        file_data = import_regex.sub('import ' + new_package_path + '.', file_data)

        # Substituir declarações de pacotes
        package_declaration = 'package ' + new_package_path + ';'
        if package_description:
            package_declaration = '/**\n * ' + package_description + '\n */\n' + package_declaration
        file_data = package_regex.sub(package_declaration, file_data)

        # Escrever de volta no arquivo
        with open(java_file, 'w') as file:
            file.write(file_data)

    print("O nome do pacote foi alterado com sucesso em todas as classes Java.")

# Exemplo de uso
rename_packages("/caminho/do/seu/projeto", ["pacote.antigo1", "pacote.antigo2"], "pacote.novo", "Este é o novo pacote.")
