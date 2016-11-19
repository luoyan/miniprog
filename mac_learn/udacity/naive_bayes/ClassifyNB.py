def classify(features_train, labels_train, method):   
    ### import the sklearn module for GaussianNB
    ### create classifier
    ### fit the classifier on the training features and labels
    ### return the fit classifier
    
    
    ### your code goes here!
    if method == 'nb':
        from sklearn.naive_bayes import GaussianNB
        clf = GaussianNB()
        clf.fit(features_train, labels_train)
        return clf
    elif method == 'lr':
        from sklearn.linear_model import LinearRegression
        clf = LinearRegression()
        clf.fit(features_train, labels_train)
        return clf
    elif method == 'logicr':
        from sklearn.linear_model import LogisticRegression
        clf = LogisticRegression()
        clf.fit(features_train, labels_train)
        return clf
    elif method == 'knn':
        from sklearn.neighbors import KNeighborsClassifier
        clf = KNeighborsClassifier()
        clf.fit(features_train, labels_train)
        return clf
    elif method == 'dtc':
        from sklearn import tree
        clf = tree.DecisionTreeClassifier()
        clf.fit(features_train, labels_train)
        return clf
    elif method == 'svm':
        from sklearn.svm import SVC
        clf = SVC()
        clf.fit(features_train, labels_train)
        return clf
    elif method == 'mlp':
        from sklearn.neural_network import MLPClassifier
        clf = MLPClassifier(solver='lbfgs', alpha=1e-5, hidden_layer_sizes=(5, 2), random_state=1)
        clf.fit(features_train, labels_train)
        return clf
    return None
    
